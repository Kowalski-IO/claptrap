// Claptrap.js
// Kowalski.io

const autoRefresh = typeof (EventSource) != undefined;
const observableBus = {
	observable : riot.observable()
};

const msgPleaseSelect = 'Please pick a mode bitch';

riot.mixin(observableBus);
riot.mount('*');
