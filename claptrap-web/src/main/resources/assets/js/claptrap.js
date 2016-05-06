// Claptrap.js
// Kowalski.io

const autoRefresh = typeof (EventSource) != undefined;
const observableBus = {
	observable : riot.observable()
};

riot.mixin(observableBus);
riot.mount('*');
