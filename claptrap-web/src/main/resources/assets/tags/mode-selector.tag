<mode-selector>

  <ul class="nav navbar-nav">
     <li class="dropdown">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">{ modeLabel }<span class="caret"></span></a>
        <ul class="dropdown-menu">
         <li class="dropdown-header">Mode Selector</li>
         <li><button class="btn btn-link" data-message="email" onclick={ modeSelect }>Email</button></li>
         <li><button class="btn btn-link" data-message="logs" onclick={ modeSelect }>Logs</button></li>
        </ul>
      </li>
  </ul>
  
  
  <script>
  
    var self = this;
    
    self.modeSelected = false;
    self.selectedMode = undefined;
    self.modeLabel = "Select a Mode";
  
    modeSelect(mode) {
        self.modeSelected = true;
        self.selectedMode = event.target.dataset.message;
        self.modeLabel = '[MODE] ' + event.target.dataset.message;
        self.update();
        self.observable.trigger('modeSelected', this.selectedMode);
    }
  
  </script>

</mode-selector>
