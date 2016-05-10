<log-viewer>

    <div class="modal fade" id="logViewer" tabindex="-1" role="dialog" aria-labelledby="logViewer">
      <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
    
          <div class="modal-header">
            <h4 class="modal-title pull-left">Log Info</h4>
            <img class="action pull-right" data-dismiss="modal" src="img/close.svg">
          </div>
    
          <div class="modal-body">
            <table class="table table-condensed">  
              <thead>
                <tr> 
                  <th>Received</th> 
                  <th>Environment</th> 
                  <th>Level</th> 
                </tr> 
              </thead>
              <tbody> 
                <tr> 
                  <td>{ log.received }</td>
                  <td>{ log.environment }</td>
                  <td>{ log.level }</td>
                </tr>
              </tbody>
            </table>
            
            <hr>
            
            <h4>Logger Message</h4>
            <span>{ log.message }</span>
            
            <hr>
            
            <h4>Throwable</h4>
            <table class="table table-condensed">  
              <thead>
                <tr> 
                  <th>Throwable Class</th> 
                  <th>Detail Message</th>  
                </tr> 
              </thead>
              <tbody> 
                <tr> 
                  <td>{ log.throwableClass }</td>
                  <td>{ log.throwable.detailMessage }</td>
                </tr>
              </tbody>
            </table>
            
            <hr>
            
            <h4>Stack Trace</h4>
             <table class="table table-condensed">  
              <thead>
                <tr> 
                  <th>File Name</th> 
                  <th>Declaring Class</th> 
                  <th>Method</th>
                  <th>Line Number</th>  
                </tr> 
              </thead>
              <tbody> 
                <tr each= { log.throwable.stackTrace} no-reorder> 
                  <td>{ fileName }</td>
                  <td>{ declaringClass }</td>
                  <td>{ methodName }</td>
                  <td>{ lineNumber }</td>
                </tr>
              </tbody>
            </table>
            
            
          </div>
    
        </div>
      </div>
    </div>

    <script>
        var self = this;
        self.log = undefined;
        
        self.observable.on('expandLog', function(logToExpand) {
            self.log = logToExpand;
            self.update();
            $('#logViewer').modal('show');
        });
        
    </script>

</log-viewer>