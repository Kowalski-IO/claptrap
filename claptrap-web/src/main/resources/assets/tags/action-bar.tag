<action-bar>

    <ul class="nav navbar-nav navbar-right">
      <li><a onclick="{ refresh }"><img src="img/refresh.svg" class="action" title="Refresh Environments & Content"></a></li>
      <li><a onclick="{ deleteAll }"><img src="img/delete-all.svg" class="action" title="Delete All for Selected Environment"></a></li>
    </ul>
    
    <script>
    
        var self = this;
        
        refresh() {
            self.observable.trigger('manualRefresh');
        }
    
        deleteAll() {
             self.observable.trigger('deleteAll');
        }
        
    </script>

</action-bar>