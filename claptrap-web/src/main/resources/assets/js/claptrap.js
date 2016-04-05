//Claptrap.js
//Kowalski.io

// Mount Tags
var serverSelectorRef;
var emailTableRef;
var emailViewerRef;
var modeSwitcherRef;

riot.compile(function() {
	serverSelectorRef = riot.mount('server-selector')[0];
	emailTableRef = riot.mount('email-table')[0];
	emailViewerRef = riot.mount('email-viewer')[0];
	// modeSwitcherRef = riot.mount('mode-switcher')[0];
});

var autoRefresh = false;

if (typeof(EventSource) !== "undefined") {
	console.log('Auto Refresh via SSE enabled.');
	autoRefresh = true;
}

var refreshComponents = function() {
	serverSelectorRef.refresh();
	emailTableRef.refresh();
}