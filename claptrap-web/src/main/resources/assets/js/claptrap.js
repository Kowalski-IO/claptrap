//Claptrap.js
//Kowalski.io

// Mount Tags
var environmentSelectorRef;
var emailTableRef;
var emailViewerRef;
var modeSwitcherRef;

riot.compile(function() {
	environmentSelectorRef = riot.mount('environment-selector')[0];
//	emailTableRef = riot.mount('email-table')[0];
//	emailViewerRef = riot.mount('email-viewer')[0];
});

var autoRefresh = false;

if (typeof(EventSource) !== "undefined") {
	console.log('Auto Refresh via SSE enabled.');
	autoRefresh = true;
}

var refreshComponents = function() {
	environmentSelectorRef.refresh();
//	emailTableRef.refresh();
}