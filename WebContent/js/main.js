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
				/*console.log(JSON.stringify(d.data))
				$.ajax({
					type: "DELETE", 
					url: "./rest/website",
					data: JSON.stringify(d.data),
					success: successCallback,
					error: errorCallback,
					contentType: "application/json",
				});*/
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
		          { data: "active" },
		          { data: "lCrawled"},
		          { data: null }
		          //Renderfunction below
		          ],
		          columnDefs: 
		        	  [
		        	   {	render: function ( data, type, row ) {
		        		   return "<a class='btn btn-primary' href='./SiteOverview/"+row.id+"'>Details</a>"
		        	   },
		        	   targets: 6 },
		        	   { orderable: false, targets: 6 },
		        	   { searchable: false, targets: 6 }
		        	   ],
		        	   select: 'single',
		        	   buttons: [
		        	             { extend: "create", editor: siteListEditor },
		        	             { extend: "edit",   editor: siteListEditor }
		        	            // { extend: "remove", editor: siteListEditor }
		        	             ]
	} );
	
	
	siteListEditor
	.on( 'initEdit', function () { siteListEditor.disable( "address" ); siteListEditor.show( "active" ); } )
	.on( 'initCreate', function () { siteListEditor.enable( "address" ); siteListEditor.hide( "active" ); } );
	 
}

function generateWordsTable() {
	wordListEditor = new $.fn.dataTable.Editor( {
		ajax: function ( method, url, d, successCallback, errorCallback ) {


			if ( d.action === 'create' ) {
				//Not implemented
			}
			else if ( d.action === 'edit' ) {
				console.log(JSON.stringify(d.data))
				var data = d.data;
				$.ajax({
					type: "PUT", 
					url: "./rest/word",
					data: JSON.stringify(data),
					success: successCallback,
					error: errorCallback,
					contentType: "application/json",
				});
			}
			else if ( d.action === 'remove' ) {
				//Not implemented
			}


		},
		idSrc: "word",
		table: "#word-list-table",
		fields: [
		{
			label: "Wordtype",
			name: "wType",
			type: "select",
			placeholder: "Select Wordtype"
		},{
			label: "Active",
			name: "active",
			type: "checkbox",
			options:   [
			            { label: '', value: 1 }
			            ]
		}
		]
	} );
	
	
	
	
	
	wordListTable = $('#word-list-table').DataTable({
		dom: "Bfrtip",
		ajax: "./rest/word", 
		order: [[ 1, "desc" ]],
		columns: [
		          { data: "word" },
		          { data: "amount" },
		          { data: "wType"},
		          { data: "active" },
		          { data: null}
		          //Render Function below
		          ],
		          columnDefs: 
		        	  [
		        	   {	render: function ( data, type, row ) {
		        		   				return "<a class='btn btn-primary' href='./WordOverview/"+row.word+"'>Details</a>"
		        	   },
		        	   targets: 34},
		        	   { orderable: false, targets: 4 },
		        	   { searchable: false, targets: 4 }
		        	   ],
		          select: 'single',
		          buttons: [{ extend: "edit",   editor: wordListEditor }]

	} );
	
	
	wordListEditor
	.on( 'initEdit', function () {  } )
	.on( 'initCreate', function () { siteListEditor.enable( "address" ); siteListEditor.hide( "active" ); } );
}
