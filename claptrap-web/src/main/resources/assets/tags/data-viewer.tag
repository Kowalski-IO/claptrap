<data-viewer>
      
      <email-table name="emailTable"></email-table>
      
<script>

  this.selectedEnvironment = undefined;
  this.selectedMode = undefined;
  
  var self = this;
  
  this.observable.on('environmentSelected', function(environment) {
    self.selectedEnvironment = environment;
    self.tags.emailTable.changeEnvironment(self.selectedEnvironment);
  });
  
  this.observable.on('modeSelected', function(mode) {
    self.selectedMode = mode;
  });
  
  
</script>
      
      
</data-viewer>