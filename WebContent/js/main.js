var siteListTable;
var siteListEditor;

var wordListTable;

$(document).ready(function () {

	// First
	generateSiteListTable();
	// Second
	generateWordsTable();
});

function generateSiteListTable() {

	siteListEditor = new $.fn.dataTable.Editor( {
		ajax: function ( method, url, d, successCallback, errorCallback ) {


			if ( d.action === 'create' ) {
				console.log(JSON.stringify(d.data[0]))
				var data = d.data[0];
				$.ajax({
					type: "POST",
					url: "./rest/website",
					data: JSON.stringify(data),
					success: successCallback,
					error: errorCallback,
					contentType: "application/json",
				});
			}
			else if ( d.action === 'edit' ) {
				console.log(JSON.stringify(d.data))
				var data = d.data;
				$.ajax({
					type: "PUT", 
					url: "./rest/website",
					data: JSON.stringify(data),
					success: successCallback,
					error: errorCallback,
					contentType: "application/json",
				});
			}
			else if ( d.action === 'remove' ) {
				console.log(JSON.stringify(d.data))
				$.ajax({
					type: "DELETE", 
					url: "./rest/website",
					data: JSON.stringify(d.data),
					success: successCallback,
					error: errorCallback,
					contentType: "application/json",
				});
			}


		},
		idSrc: "id",
		table: "#site-list-table",
		fields: [ {
			label: "Adress:",
			name: "address"
		}, {
			label: "Description",
			name: "description"
		}, {
			label: "Crawldepth",
			name: "depth"
		}, {
			label: "Active",
			name: "active",
			type: "checkbox",
			options:   [
			            { label: '', value: 1 }
			            ]
		}
		]
	} );



	//console.log(tableData);
	siteListTable = $('#site-list-table').DataTable({
		dom: "Bfrtip",
		ajax: "./rest/website", 
		columns: [
		          { data: "id" },
		          { data: "address" },
		          { data: "description" },
		          { data: "depth" },
		          { data: "active" }
		          //add render function for button
		          ],
		          select: 'single',
		          buttons: [
		                    { extend: "create", editor: siteListEditor },
		                    { extend: "edit",   editor: siteListEditor },
		                    { extend: "remove", editor: siteListEditor }
		                    ]
	} );
}

function generateWordsTable() {
	wordListTable = $('#word-list-table').DataTable({
		dom: "frtip",
		ajax: "./rest/word",
		order: [[ 1, "desc" ]],
		columns: [
		          { data: "word" },
		          { data: "amount" },
		          { data: "active" },
		          //add render function
		          ],
		          select: 'single'
		         
	} );
}
