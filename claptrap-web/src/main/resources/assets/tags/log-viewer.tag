<log-viewer>

    <div class="modal fade" id="logViewer" tabindex="-1" role="dialog" aria-labelledby="logViewer">
      <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
    
          <div class="modal-header">
            <h4 class="modal-title pull-left">{ log.throwableClass }</h4>
            <img class="action pull-right" data-dismiss="modal" src="img/close.svg">
          </div>
    
          <div class="modal-body">
            <table class="table table-condensed">  
              <thead>
                <tr> 
                  <th>Received</th> 
                  <th>Sender</th> 
                  <th>Recipient</th> 
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