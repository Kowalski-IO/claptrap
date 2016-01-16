<email-viewer>

<div class="modal fade" id="emailViewer" tabindex="-1" role="dialog" aria-labelledby="emailViewer">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">{ email.subject }</h4>
      </div>
      <div class="modal-body">
        <table class="table table-condensed">  
            <thead>
                <tr> 
                    <th>Received</th> 
                    <th>Sender</th> 
                    <th>Recipient(s)</th> 
                </tr> 
            </thead>
            <tbody> 
                <tr> 
                    <td>{ email.receivedOn }</td>
                    <td>{ email.sender }</td>
                    <td><span each ={ email.recipients }>{ address }; </span></td>
                </tr>
            </tbody>
        </table>
      
      <hr>
      
      <div id="email-body" if = { email.htmlEmail }>
      
        <div>

          <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href="#html" aria-controls="html" role="tab" data-toggle="tab">HTML Email</a></li>
            <li role="presentation"><a href="#plain" aria-controls="plain" role="tab" data-toggle="tab">Plain Email</a></li>
          </ul>
          
          <br>

          <div class="tab-content">
            <div role="tabpanel" class="tab-pane active" id="html"><raw-tag content={ email.htmlBody }/></div>
            <div role="tabpanel" class="tab-pane" id="plain">{ email.plainBody }</div>
          </div>

        </div>

      </div>
      
      <div id="email-body" if = { !email.htmlEmail }>
        
        {email.plainBody}
      
      </div>

      </div>
    </div>
  </div>
</div>


    <script>
        var self = this;
        var email;
        
        showEmail(emailToView) {
            self.email = emailToView;
            self.update();
            $('#emailViewer').modal('show');
        }
        
    </script>

</email-viewer>