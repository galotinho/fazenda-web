$(document).ready(function () {
	moment.locale('pt-BR');
    var table = $('#table-fluxo').DataTable({
    	language: {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Portuguese-Brasil.json"
        },
    	searching: true,
    	order: [[ 1, "asc" ]],
    	lengthMenu: [5, 10],
        processing: true,
        serverSide: true,
        responsive: true,
        ajax: {
            url: '/fluxo/datatables/server',
            data: 'data'
        },
        columns: [
            {data: 'id'},
            {data: 'data', render:
                function( data ) {
                    return moment(data).format('LL');
                }
            },
            {data: 'propriedade.nome'},
            {data: 'item'},
            {data: 'valor',
            	render: $.fn.dataTable.render.number( '.', ',', 2 )	
            },
            {data: 'natureza'},
            {orderable: false, 
             data: 'id',
                "render": function(id) {
                    return '<a class="btn btn-success btn-sm btn-block" href="/fluxo/editar/'+ 
                    	id +'" role="button"><i class="fas fa-edit"></i></a>';
                }
            },
            {orderable: false,
             data: 'id',
                "render": function(id) {
                    return '<a class="btn btn-danger btn-sm btn-block" href="/fluxo/excluir/'+ 
                    	id +'" role="button" data-toggle="modal" data-target="#confirm-modal"><i class="fas fa-times-circle"></i></a>';
                }               
            }
        ],
     });
});    
