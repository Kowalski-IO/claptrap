var queryParams = [], hash;

var loadEmail = function(server, uuid) {
	$.ajax({
		type : 'GET',
		url : 'email/' + server + '/' + uuid,
		dataType : 'json',
		success : function(data) {
			showEmail(data);
		},
		error : function(xhr, type) {
			alert('Claptrap was unable to retreive this email.');
		}
	});
};

var showEmail = function(data) {
	
	$('#no-email-found').hide();
	$('#email-container').show();
	
	var received = $('#received');
	var from = $('#from');
	var recipient = $('#recipient');
	var subject = $('#subject');
	var body = $('#body');
			
	received.append($('<h5>', {
		html : data.received
	}));
	
	from.append($('<h5>', {
		html : data.from
	}));
	
	recipient.append($('<h5>', {
		html : data.recipient
	}));
	
	subject.append($('<h5>', {
		html : data.subject
	}));
	
	body.append($('<h6>', {
		html : data.body
	}));

};

$(document).ready(function() {
	$('#email-container').hide();
	
	var q = document.URL.split('?')[1];
	if (q != undefined) {
		q = q.split('&');
		for (var i = 0; i < q.length; i++) {
			hash = q[i].split('=');
			queryParams.push(hash[1]);
			queryParams[hash[0]] = hash[1];
		}
	};

	if (queryParams['server'] != undefined && queryParams['email'] != undefined) {
		loadEmail(queryParams['server'], queryParams['email']);
	}
});