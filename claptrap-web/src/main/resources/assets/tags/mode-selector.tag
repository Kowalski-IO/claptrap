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
    
    this.modeSelected = false;
    this.selectedMode = undefined;
    this.modeLabel = "Select a Mode";
  
    modeSelect(mode) {
        this.modeSelected = true;
        this.selectedMode = event.target.dataset.message;
        this.modeLabel = '[MODE] ' + event.target.dataset.message;
        this.update();
        this.observable.trigger('modeSelected', this.selectedMode);
    }
  
  </script>


</mode-selector>
