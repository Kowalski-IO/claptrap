<email-viewer>

<div class="modal fade" id="emailViewer" tabindex="-1" role="dialog" aria-labelledby="emailViewer">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title pull-left">{ email.subject }</h4>
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
                    <td>{ email.received }</td>
                    <td>{ email.sender }</td>
                    <td>{ email.recipient }</td>
                </tr>
            </tbody>
        </table>
      
          <hr>
          
          <div id="email-body" if = { email.htmlEmail }>
    
          <div>
        
            <ul class="nav nav-tabs" role="tablist">
              <li role="presentation" class="active"><a href="#plain" aria-controls="plain" role="tab" data-toggle="tab">Plain Email</a></li>
            </ul>
        
            <br>
        
            <div class="tab-content">
              <div role="tabpanel" class="tab-pane active" id="plain">{ email.plainBody }</div>
            </div>
        
          </div>
        
        </div>
        
        <div id="email-body" if = { !email.htmlEmail }>
          <raw-tag content={ email.plainBody }/>
        </div>
      

      </div>
    </div>
  </div>
</div>


    <script>
        var self = this;
        self.email = undefined;
        
        this.observable.on('expandEmail', function(emailToExpand) {
            self.email = emailToExpand;
            self.update();
            $('#emailViewer').modal('show');
        });
        
    </script>

</email-viewer>