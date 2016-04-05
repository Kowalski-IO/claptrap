<email-table>

    <div if ={ serverSelectorRef.serverSelected === false }>
        <h2 class="email-table-message">Please pick a server from the selector above.</h2>
        <object type="image/svg+xml" class="claptrap-logo" data="img/claptrap.svg">Your browser does not support SVG</object>
    </div>

    <div if ={ serverSelectorRef.serverSelected === true && emails.length === 0 }>
        <h2 class="email-table-message">No emails caught for selected server.</h2>
        <object type="image/svg+xml" class="claptrap-logo" data="img/claptrap.svg">Your browser does not support SVG</object>
    </div>
    
    <div if = { serverSelectorRef.serverSelected === true && emails.length > 0 } class="table-responsive">
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
	                    <a onclick="{ parent.expand }"><span class="glyphicon glyphicon-new-window info" title="Read"></span></a>&nbsp;
	                    <a onclick="{ parent.delete }"><span class="glyphicon glyphicon-trash danger" title="Delete"></span></a>
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
        
        serverChange() {
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
            var serverName = serverSelectorRef.getSelectedServer();
           $.get('./api/emails/' + serverName, function(data) {
                self.emails = data;
                self.update();
            });
        }
        
        watch() {
        	var serverName = serverSelectorRef.getSelectedServer();
        	if (serverName === undefined || autoRefresh === false) {
        		return;
        	} else if (self.watchEventSource !== undefined) {
        		self.watchEventSource.close();
        	}
        	self.watchEventSource = new EventSource('./api/emails/broadcast/' + serverName);
        	self.watchEventSource.onmessage = function(message) {
        		   var newEmail = message.data;
        		   self.emails.unshift(JSON.parse(newEmail));
        		   self.update();
        	}
        }
        
        deleteAllEmails() {
        	 var serverName = serverSelectorRef.getSelectedServer();
             var deleteUrl = './api/emails/' + serverName;
             $.ajax({
                url: deleteUrl,
                type: 'DELETE',
                complete: function(data) { self.refresh(); }
            });
        }
        
        ajaxDeleteEmail(emailToDelete) {
        	 var serverName = serverSelectorRef.getSelectedServer();
        	 var deleteUrl = './api/emails/' + serverName + '/' + emailToDelete;
        	 $.ajax({
       		    url: deleteUrl,
       		    type: 'DELETE'
       		});
        }
    
    </script>  
    
</email-table>