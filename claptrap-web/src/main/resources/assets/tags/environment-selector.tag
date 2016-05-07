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
        <p class="navbar-text">No Environments Captured</p>
      </li>
  </ul>

  <script>
  
  var self = this;
  
  self.environmentSelected = false;
  self.selectedEnvironment = undefined;
  self.environmentLabel = "Select an Environment";

  self.environments = [];
  
  self.on('mount', function() {
    self.refresh();
  });
  
  self.observable.on('manualRefresh', function(mode) {
    self.refresh();
  });
  
  refresh() {
       $.get('./api/environments', function(data) { 
            self.environments = data;
            self.update();
        });
    }
    
  environmentSelect(event) {
    self.environmentSelected = true;
    self.selectedEnvironment = event.target.dataset.message;
    self.environmentLabel = '[ENV] ' + event.target.dataset.message;
    self.update();
    self.observable.trigger('environmentSelected', this.selectedEnvironment);
  }
  
  </script>

</environment-selector>
