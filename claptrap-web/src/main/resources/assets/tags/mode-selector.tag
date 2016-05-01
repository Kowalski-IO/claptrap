<mode-selector>

  <ul class="nav navbar-nav">
     <li class="dropdown">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Select a Mode<span class="caret"></span></a>
        <ul class="dropdown-menu">
         <li class="dropdown-header">Mode Selector</li>
         <li><button class="btn btn-link" data-message="email" onclick={ environmentSelect }>Email</button></li>
         <li><button class="btn btn-link" data-message="logs" onclick={ environmentSelect }>Logs</button></li>
        </ul>
      </li>
  </ul>


</mode-selector>