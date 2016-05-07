<email-table>

    <div if ={ emails.length === 0 }>
        <h2 class="table-message">No emails captured for selected environment.</h2>
    </div>
    
    <div if = { emails.length > 0 } class="table-responsive">
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
	                    <a onclick="{ parent.expand }"><img class="action-icon" src="img/open.svg" title="read" /></span></a>
	                    <a onclick="{ parent.delete }"><img class="action-icon" src="img/delete.svg" title="delete" /></span></a>
	               </td>
	            </tr>
	        </tbody>
	    </table>
    </div>
    
    <script>
        
         this.selectedEnvironment = undefined;
        
        var self = this;
        var emails = [];
        var watchEventSource;

        expand(event) {
        	emailViewerRef.showEmail(event.item);
        }
        
        changeEnvironment(environment) {
            self.selectedEnvironment = environment;
            self.refresh();
            self.watch();
        }
        
        delete(event) {
        	var email = event.item;
        	self.ajaxDeleteEmail(email.id);
        	var index = self.emails.indexOf(email)
        	self.emails.splice(index, 1)
        }
  
        refresh() {
            if (self.selectedEnvironment == undefined) {
                return;
            }
            
           $.get('./api/emails/' + self.selectedEnvironment, function(data) {
                self.emails = data;
                self.update();
            });
        }
        
        watch() {
        	if (self.selectedEnvironment === undefined || autoRefresh === false) {
        		return;
        	} else if (self.watchEventSource !== undefined) {
        		self.watchEventSource.close();
        	}
        	self.watchEventSource = new EventSource('./api/emails/broadcast/' + self.selectedEnvironment);
        	self.watchEventSource.onmessage = function(message) {
        		   var newEmail = message.data;
        		   self.emails.unshift(JSON.parse(newEmail));
        		   self.update();
        	}
        }
        
        deleteAllEmails() {
             var deleteUrl = './api/emails/' + self.selectedEnvironment;
             $.ajax({
                url: deleteUrl,
                type: 'DELETE',
                complete: function(data) { self.refresh(); }
            });
        }
        
        ajaxDeleteEmail(emailToDelete) {
        	 var deleteUrl = './api/emails/' + self.selectedEnvironment + '/' + emailToDelete;
        	 $.ajax({
       		    url: deleteUrl,
       		    type: 'DELETE'
       		});
        }
    
    </script>  
    
</email-table>
