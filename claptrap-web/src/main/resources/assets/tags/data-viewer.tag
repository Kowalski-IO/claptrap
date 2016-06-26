<data-viewer>
      
      <h2 class="table-message" if = { selectedMode == undefined || selectedEnvironment == undefined }>Please select an environment and mode.</h2>
      
      <email-table if = { selectedMode == 'email' && selectedEnvironment != undefined } name="emailTable"></email-table>
      <log-table if = { selectedMode == 'logs' && selectedEnvironment != undefined } name="logTable"></log-table>
      
<script>

  var self = this;
  
  self.selectedEnvironment = undefined;
  self.selectedMode = undefined;
  
  self.observable.on('environmentSelected', function(environment) {
    self.selectedEnvironment = environment;
    self.tags.emailTable.changeEnvironment(self.selectedEnvironment);
    self.tags.logTable.changeEnvironment(self.selectedEnvironment);
    self.update();
  });
  
  self.observable.on('modeSelected', function(mode) {
    self.selectedMode = mode;
     switch (self.selectedMode) {
        case "email":
            self.tags.emailTable.refresh();
            break;
        case "logs":
            self.tags.logTable.refresh();
            break;
    }
    self.update();
  });
  
  self.observable.on('manualRefresh', function() {
    self.tags.emailTable.refresh();
    self.tags.logTable.refresh();
  });
  
  self.observable.on('deleteAll', function() {
    switch (self.selectedMode) {
        case "email":
            self.tags.emailTable.deleteAllEmails();
            break;
        case "logs":
            self.tags.logTable.deleteAllLogs();
            break;
    }
  });
  
  self.observable.on('updateFilter', function(filterText) {
    switch (self.selectedMode) {
        case "email":
            self.tags.emailTable.filter(filterText);
            break;
        case "logs":
            self.tags.logTable.filter(filterText);
            break;
    }
  });
  
</script>
      
</data-viewer>
