<log-table>

    <div if ={ logs.length === 0 }>
        <h2 class="table-message">No logs captured for selected environment.</h2>
    </div>
    
    <div if = { logs.length > 0 } class="table-responsive">
        <table class="table table-striped table-bordered table-hover table-condensed">  
            <thead>
                <tr> 
                    <th>Received</th> 
                    <th>Level</th> 
                    <th>Declaring Class</th> 
                    <th>Line Number</th>
                    <th>Thrown Exception Class</th>
                    <th>Actions</th>
                </tr> 
            </thead>
            <tbody> 
                <tr each = { logs }> 
                    <td class="col-md-2">{ received }</td>
                    <td class="col-md-1">{ level }</td>
                    <td class="col-md-3">{ throwable.stackTrace[0].declaringClass }</td>
                    <td class="col-md-1">{ throwable.stackTrace[0].lineNumber }</td>
                    <td class="col-md-5">{ throwableClass }</td>
                    <td class="col-md-1 text-center">
                        <a onclick="{ parent.expandLog }"><img class="action-icon action" src="img/open.svg" title="read" /></span></a>
                        <a onclick="{ parent.deleteLog }"><img class="action-icon action" src="img/delete.svg" title="delete" /></span></a>
                   </td>
                </tr>
            </tbody>
        </table>
    </div>
    
     <script>
     
        var self = this;
        
        self.selectedEnvironment = undefined;
        
        self.logs = [];
        self.watchEventSource;

        expandLog(event) {
            self.observable.trigger('expandLog', event.item);
        }
        
        changeEnvironment(environment) {
            self.selectedEnvironment = environment;
            self.refresh();
            self.watch();
        }
        
        deleteLog(event) {
            var log = event.item;
            self.ajaxDeleteLog(log.id);
            var index = self.logs.indexOf(log)
            self.logs.splice(index, 1)
        }
  
        refresh() {
            if (self.selectedEnvironment == undefined) {
                return;
            }
            
           $.get('./api/logs/' + self.selectedEnvironment, function(data) {
                self.logs = data;
                self.update();
            });
        }
        
        watch() {
            if (self.selectedEnvironment === undefined || autoRefresh === false) {
                return;
            } else if (self.watchEventSource !== undefined) {
                self.watchEventSource.close();
            }
            self.watchEventSource = new EventSource('./api/logs/broadcast/' + self.selectedEnvironment);
            self.watchEventSource.onmessage = function(message) {
               var newLog = message.data;
               self.logs.unshift(JSON.parse(newLog));
               self.update();
            }
        }
        
        deleteAllLogs() {
             var deleteUrl = './api/logs/' + self.selectedEnvironment;
             $.ajax({
                url: deleteUrl,
                type: 'DELETE',
                complete: function(data) { self.refresh(); }
            });
        }
        
        ajaxDeleteLog(logToDelete) {
             var deleteUrl = './api/logs/' + self.selectedEnvironment + '/' + logToDelete;
             $.ajax({
                url: deleteUrl,
                type: 'DELETE'
            });
        }
    
    </script>  

</log-table>