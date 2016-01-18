var siteListTable;
var siteListEditor;

var wordListTable;

$(document).ready(function () {

	// First
	generateSiteListTable();
	// Second
	generateWordsTable();

});

//Reload the page after user dismisses the modal (wrong URL alert)
$('#url-alert-modal').on('hidden.bs.modal', function(e){
	location.reload();
});


function generateSiteListTable() {

	siteListEditor = new $.fn.dataTable.Editor( {
		ajax: function ( method, url, d, successCallback, errorCallback ) {


			if ( d.action === 'create' ) {
				//console.log(JSON.stringify(d.data[0]));
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
	
	siteListEditor.on( 'preSubmit', function ( e, o, action ) {
       console.log("Checking");
		if ( action === 'create' ) {
			console.log("in if");
            var url = siteListEditor.field( 'address' );
            var depth = siteListEditor.field('depth');
            console.log(url.val());
 
            //Beware of CORS ;)
            $.ajax({ cache: false,
                url: url.val(),
                async: false
            }).done(function (data) {
                console.log("success");
            }).fail(function (jqXHR, textStatus) {
               url.error("Invalid URL: Maybe add slash at the end");
            });
            
            if(depth.val()<1){
            	depth.error("Depth must be at least 1");
            }
            
            // If any error was reported, cancel the submission so it can be corrected
            if ( this.inError() ) {
            	console.log("inerror");
                return false;
            }
        }
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
				console.log(JSON.stringify(d.data));
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
		idSrc: "word.word",
		table: "#word-list-table",
		fields: [
		{
			label: "Wordtype",
			name: "word.wType",
			type: "select",
			placeholder: "Select Wordtype"
		},{
			label: "Active",
			name: "word.active",
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
		          { data: "word.word" },
		          { data: "word.amount" },
		          { data: "wTypes.name"},
		          { data: "word.active" },
		          { data: null}
		          //Render Function below
		          ],
		          columnDefs: 
		        	  [
		        	   {	render: function ( data, type, row ) {
		        		   				return "<a class='btn btn-primary' href='./WordOverview/"+row.word.word+"'>Details</a>"
		        	   },
		        	   targets: 4},
		        	   { orderable: false, targets: 4 },
		        	   { searchable: false, targets: 4 }
		        	   ],
		          select: 'single',
		          buttons: [{ extend: "edit",   editor: wordListEditor }]

	} );
	
}
