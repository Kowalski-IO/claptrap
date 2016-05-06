<environment-selector>

  <ul class="nav navbar-nav navbar-left">
    <li if = { environments.length > 0 } class="dropdown">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">{ environmentLabel }<span class="caret"></span></a>
        <ul class="dropdown-menu">
         <li class="dropdown-header">Environment Selector</li>
         <li each={ name, environment in environments }><button class="btn btn-link" data-message="{ name }" onclick={ environmentSelect }>{ name }</button></li>
        </ul>
      </li>
      <li if = { environments.length === 0 } >
        <p class="navbar-text">No Environments Found</p>
      </li>
  </ul>

  <script>
  
  var self = this;
  
  this.environmentSelected = false;
  this.selectedEnvironment = undefined;
  this.environmentLabel = "Select an Environment";

  this.environments = [];
  
  this.on('mount', function() {
    self.refresh();
  });
  
  refresh() {
       $.get('./api/environments', function(data) { 
            self.environments = data;
            self.update();
        });
    }
    
    getSelectedEnvironment() {
        return this.selectedEnvironment
    }
  
  environmentSelect(event) {
    this.environmentSelected = true;
    this.selectedEnvironment = event.target.dataset.message;
    this.environmentLabel = '[ENV] ' + event.target.dataset.message;
    this.update();
    emailTableRef.environmentChange();
  }

  </script>

</environment-selector>