//Claptrap.js
//Kowalski.io

// Mount Tags
var environmentSelectorRef;
var modeSelectorRef;
var emailTableRef;
var emailViewerRef;

riot.compile(function() {
	environmentSelectorRef = riot.mount('environment-selector')[0];
	modeSelectorRef = riot.mount('mode-selector')[0];
	emailTableRef = riot.mount('email-table')[0];
	emailViewerRef = riot.mount('email-viewer')[0];
});

var autoRefresh = false;

if (typeof(EventSource) !== "undefined") {
	console.log('Auto Refresh via SSE enabled.');
	autoRefresh = true;
}

var refreshComponents = function() {
	environmentSelectorRef.refresh();
	emailTableRef.refresh();
}