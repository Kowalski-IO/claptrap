<server-selector>

  <ul class="nav navbar-nav navbar-left">
    <li if = { servers.length > 0 } class="dropdown">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">{ serverSelectLabel }<span class="caret"></span></a>
        <ul class="dropdown-menu">
         <li class="dropdown-header">Server Selector</li>
         <li each={ name, server in servers }><button class="btn btn-link" data-message="{ name }" onclick={ serverSelect }>{ name }</button></li>
        </ul>
      </li>
      <li if = { servers.length === 0 } >
        <p class="navbar-text">No Servers Found</p>
      </li>
    </ul>

  <script>
  
  var self = this;
  
  this.serverSelected = false;
  this.selectedServer = undefined;
  this.serverSelectLabel = "Select a Server";
  this.severSelectLabelPrefix = "Server: ";


  this.servers = [];
  
  this.on('mount', function() {
  	self.refresh();
  });
  
  refresh() {
       $.get('./api/servers', function(data) { 
            self.servers = data;
            self.update();
        });
    }
    
    getSelectedServer() {
        return this.selectedServer;
    }
  
  serverSelect(event) {
    this.serverSelected = true;
  	this.selectedServer = event.target.dataset.message;
  	this.serverSelectLabel = this.severSelectLabelPrefix + event.target.dataset.message;
  	this.update();
  	emailTableRef.serverChange();
  }

  </script>

</server-selector>