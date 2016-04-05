<mode-switcher>

	<div class="navbar-form modeSwitcherContainer">
		<input id="modeSwitcher" type="checkbox" checked>
	</div>
	
	<script>
	
	   var self = this;
	   
	   var mode = 'email';

	    this.on('mount', function(){
			$("#modeSwitcher").bootstrapSwitch({"size": "mini", "onText": "Email", 
				"offText": "SMS", "onColor": "info", "offColor": "success", "onSwitchChange": self.switchChange});
		  });
	    
	    switchChange(event, state) {
	    	if (state) {
	    		mode = 'email';
	    	} else {
	    		mode = 'sms';
	    	}
	    }
	    
	    getMode() {
	    	return mode;
	    }
	</script>

</mode-switcher>