//Claptrap.js

var queryParams = [], hash;

var loadEmails = function() {
	var server = queryParams['server'] || $('#server-list').val();
	$.ajax({
		type : 'GET',
		url : 'emails/' + server,
		dataType : 'json',
		success : function(data) {
			buildTable(data);
		},
		error : function(xhr, type) {
			alert('Claptrap was unable to retrieve list of emails for server '
					+ server + '.');
		}
	});
};

var loadServers = function() {
	$.ajax({
		type : 'GET',
		url : 'servers',
		dataType : 'json',
		success : function(data) {
			buildServerList(data);
		},
		error : function(xhr, type) {
			alert('Claptrap was unable to retreive list of servers.');
		}
	});
};

var deleteEmail = function(server, emailId) {
	$.ajax({
		type : 'DELETE',
		url : 'email/' + server + '/' + emailId,
		dataType : 'json',
		success : function(data) {
			$('#' + emailId).remove();
		},
		error : function(xhr, type) {
			alert('Claptrap was unable to delete the email.');
		}
	});
};

var deleteAllEmails = function(server) {
	$.ajax({
		type : 'DELETE',
		url : 'email/' + server,
		dataType : 'json',
		success : function(data) {
			buildTable([]);
		},
		error : function(xhr, type) {
			alert('Claptrap was unable to delete all emails for the server '
					+ server);
		}
	});
};

var buildTable = function(data) {
	$('#email-table').show();
	var server = queryParams['server'] || $('#server-list').val();
	var table_obj = $('#email-table-body');
	table_obj.empty();
	$.each(data, function(index, item) {
		var table_row = $('<tr>', {
			id : item.uuid
		});
		table_row.append($('<td>', {
			html : item.received
		}).addClass('date'));
		table_row.append($('<td>', {
			html : item.from
		}).addClass('from'));
		table_row.append($('<td>', {
			html : item.recipient
		}).addClass('recipient'));
		table_row.append($('<td>', {
			html : item.subject
		}).addClass('subject'));
		table_row.append($('<td>', {
			html : item.body
		}).addClass('body'));

		
		var view_btn = $('<button>', {
			id : 'viewbtn' + item.uuid,
			text : 'View'
		});
		
		view_btn.bind('click', function() {
			viewEmail(server, item.uuid);
		});
		
		table_row.append($('<td>', {
			html : view_btn
		}));
		
		var delete_btn = $('<button>', {
			id : 'delbtn' + item.uuid,
			text : 'Delete'
		});
		delete_btn.bind('click', function() {
			deleteEmail(server, item.uuid);
		});

		table_row.append($('<td>', {
			html : delete_btn
		}));

		table_obj.append(table_row);
	});

	if (data.length < 1) {
		$('#search-box').hide();
		$('#delete-all-button').hide();
		var table_row = $(
				'<tr>',
				{
					html : 'No emails have been caught for this server (or they have all been deleted).'
				});
		table_obj.append(table_row);
	} else {
		$('#search-box').show();
		$('#delete-all-button').show();
		var options = {
			valueNames : [ 'date', 'from', 'recipient', 'subject', 'body' ]
		};

		var hackerList = new List('claptrap-container', options);
	}
};

var buildServerList = function(data) {
	var select_obj = $('#server-list');
	select_obj.empty();
	
	var server_item = $('<option>', {
		selected : true
	});
	server_item.text('Select a server');
	select_obj.append(server_item);
	
	$.each(data, function(index, item) {
		var server_item = $('<option>', {
			value : item
		});
		server_item.text(item);
		select_obj.append(server_item);
	});

	if (data.length < 1) {
		$('#no-servers-found').show();
		$('#server-select').hide();
		$('#email-table').hide();
		console.log('No emails have been caught yet.');
	} else {
		$('#no-servers-found').hide();
		$('#server-select').show();
		$('#reload-email-button').show();
	}

};

var viewEmail = function(server, uuid) {
	window.open("email.html?server=" + server + '&email=' + uuid);
};

var deleteAllPrompt = function() {
	if (confirm('Are you sure you want to delete all these beautiful emails?')) {
		deleteAllEmails(queryParams['server'] || $('#server-list').val());
	} 
};

$(document).ready(function() {
	console.log('       ,');
	console.log('       |');
	console.log('    ]  |.-._');
	console.log('     \\|"(0)"| _]');
	console.log('     `|=\\#/=|\\/');
	console.log('      :  _  :');
	console.log('       \\/_\\/ ');
	console.log('        |=| ');
	console.log('        `-\' Just follow the soothing sound of my voice!');

	var aaaand_open = new Konami(function() {
		deleteAllEmails(queryParams['server'] || $('#server-list').val());
	});

	$('#no-servers-found').hide();
	$('#server-select').hide();
	$('#email-table').hide();
	$('#search-box').hide();
	$('#reload-email-button').hide();
	$('#delete-all-button').hide();
	
	$('#server-list').change(function() {
		loadEmails();
	});
	
	var q = document.URL.split('?')[1];
	if (q != undefined) {
		q = q.split('&');
		for (var i = 0; i < q.length; i++) {
			hash = q[i].split('=');
			queryParams.push(hash[1]);
			queryParams[hash[0]] = hash[1];
		}
	};

	if (queryParams['server'] != undefined) {
		$('#selected-server').append($('<h4>', {
			html : 'Server: ' + queryParams['server']
		}));
		console.log('Loading page for query param server '
				+ queryParams['server'] + '.');
		$('#reload-email-button').show();
		loadEmails();
	} else {
		loadServers();
	}
});