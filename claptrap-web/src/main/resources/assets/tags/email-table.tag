<email-table>

    <div if ={ environmentSelectorRef.environmentSelected === false }>
        <h2 class="email-table-message">Please pick an environment from the selector above.</h2>
        <object type="image/svg+xml" class="claptrap-logo" data="img/claptrap.svg">Your browser does not support SVG</object>
    </div>

    <div if ={ environmentSelectorRef.environmentSelected === true && emails.length === 0 }>
        <h2 class="email-table-message">No emails caught for selected environment.</h2>
        <object type="image/svg+xml" class="claptrap-logo" data="img/claptrap.svg">Your browser does not support SVG</object>
    </div>
    
    <div if = { environmentSelectorRef.environmentSelected === true && emails.length > 0 } class="table-responsive">
	    <table class="table table-striped table-bordered table-hover table-condensed">  
	        <thead>
	            <tr> 
	                <th>Received</th> 
	                <th>Sender</th> 
	                <th>Recipient</th> 
	                <th>Subject</th>
	                <th>Body</th>
	                <th>Actions</th>
	            </tr> 
	        </thead>
	        <tbody> 
	            <tr each = { emails }> 
	                <td class="col-md-2">{ received }</td>
	                <td class="col-md-1">{ sender }</td>
	                <td class="col-md-3">{ recipient }</td>
	                <td class="col-md-2">{ subject }</td>
	                <td class="col-md-4">{ plainBody }</td>
	                <td class="col-md-1">
	                    <a onclick="{ parent.expand }"><img class="action-icon" src="img/open.svg" title="read" /></span></a>&nbsp;
	                    <a onclick="{ parent.delete }"><img class="action-icon" src="img/delete.svg" title="delete" /></span></a>
	               </td>
	            </tr>
	        </tbody>
	    </table>
    </div>
    
    <script>
    
        var self = this;
        var emails = [];
        var watchEventSource;
        
        this.on('mount', function() {
            self.refresh();
          });
        
        environmentChange() {
        	self.refresh();
        	self.watch();
        }
        
        expand(event) {
        	emailViewerRef.showEmail(event.item);
        }
        
        delete(event) {
        	var email = event.item;
        	self.ajaxDeleteEmail(email.id);
        	var index = self.emails.indexOf(email)
        	self.emails.splice(index, 1)
        }
  
        refresh() {
            var environmentName = environmentSelectorRef.getSelectedEnvironment();
           $.get('./api/emails/' + environmentName, function(data) {
                self.emails = data;
                self.update();
            });
        }
        
        watch() {
        	var environmentName = environmentSelectorRef.getSelectedEnvironment();
        	if (environmentName === undefined || autoRefresh === false) {
        		return;
        	} else if (self.watchEventSource !== undefined) {
        		self.watchEventSource.close();
        	}
        	self.watchEventSource = new EventSource('./api/emails/broadcast/' + environmentName);
        	self.watchEventSource.onmessage = function(message) {
        		   var newEmail = message.data;
        		   self.emails.unshift(JSON.parse(newEmail));
        		   self.update();
        	}
        }
        
        deleteAllEmails() {
        	 var environmentName = environmentSelectorRef.getSelectedEnvironment();
             var deleteUrl = './api/emails/' + environmentName;
             $.ajax({
                url: deleteUrl,
                type: 'DELETE',
                complete: function(data) { self.refresh(); }
            });
        }
        
        ajaxDeleteEmail(emailToDelete) {
        	 var environmentName = environmentSelectorRef.getSelectedEnvironment();
        	 var deleteUrl = './api/emails/' + environmentName + '/' + emailToDelete;
        	 $.ajax({
       		    url: deleteUrl,
       		    type: 'DELETE'
       		});
        }
    
    </script>  
    
</email-table>