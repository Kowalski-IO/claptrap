<email-table>

    <div if ={ emails.length === 0 }>
        <h2 class="table-message">No emails captured for selected environment.</h2>
    </div>
    
    <div id="emailTable" if = { emails.length > 0 } class="table-responsive">
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
	        <tbody class="list"><tr each = { emails } no-reorder> 
	                <td class="col-md-2 received">{ received }</td>
	                <td class="col-md-1">{ sender }</td>
	                <td class="col-md-3">{ recipient }</td>
	                <td class="col-md-2">{ subject }</td>
	                <td class="col-md-4"><raw-tag content={ plainBody }/></td>
	                <td class="col-md-1 text-center">
	                    <a if = { htmlEmail} href= { 'email_frame.html?env=' + environment + '&email=' + id } target="_new"><img class="action-icon" src="img/open.svg" title="read" /></a>
	                    <a if = { !htmlEmail } onclick="{ parent.expandEmail }"><img class="action-icon" src="img/open.svg" title="read" /></span></a>
	                    <a onclick="{ parent.deleteEmail }"><img class="action-icon" src="img/delete.svg" title="delete" /></span></a>
	               </td>
	            </tr>
	        </tbody>
	    </table>
    </div>
    
    <script>
    
        var self = this;
        
        self.selectedEnvironment = undefined;
        
        self.emails = [];
        self.watchEventSource;
        
        expandEmail(event) {
            self.observable.trigger('expandEmail', event.item);
        }
        
        changeEnvironment(environment) {
            self.selectedEnvironment = environment;
            self.refresh();
            self.watch();
        }
        
        deleteEmail(event) {
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
    		   self.emails.unshift(JSON.parse(message.data));
    		   self.update();
        	}
        }
        
        deleteAllEmails() {
             var deleteUrl = './api/emails/' + self.selectedEnvironment;
             $.ajax({
                url: deleteUrl,
                type: 'DELETE',
                complete: function(data) {
                    self.emails = []; 
                    self.refresh();
                }
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
