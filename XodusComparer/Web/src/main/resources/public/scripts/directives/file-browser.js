app.directive('filebrowser', function ($http) {
    return {
        restrict: 'A',
        link: function (scope, element) {
            element.addClass('ag-file-browser');

            var columnDefs = [
                {
                    headerName: "Name",
                    field: "name",
                    width: 500,
                    cellRenderer: 'group',
                    cellRendererParams: {
                        innerRenderer: function (params) {
                            var image;
                            if (params.node.group) {
                                image = params.node.level === 0 ? 'disk' : 'folder';
                            } else {
                                image = 'file';
                            }
                            var imageFullUrl = '/img/' + image + '.png';
                            return '<img src="' + imageFullUrl + '" style="padding-left: 4px;" /> ' + params.data.name;
                        }
                    }
                },
                {
                    headerName: "Size",
                    field: "size",
                    width: 100,
                    cellStyle: function () {
                        return {'text-align': 'right'};
                    }
                },
                {
                    headerName: "Date Modified",
                    field: "dateModified",
                    width: 150
                }
            ];

            var currentItem = {};
            var rowData = [];

            var gridOptions = {
                columnDefs: columnDefs,
                rowData: rowData,
                rowSelection: 'multiple',
                enableColResize: true,
                enableSorting: true,
                rowHeight: 20,
                getNodeChildDetails: function (file) {
                    if (file.folder) {
                        return {
                            group: true,
                            children: file.children,
                            expanded: file.open
                        };
                    } else {
                        return null;
                    }
                },
                icons: {
                    groupExpanded: '<i class="fa fa-minus-square-o"/>',
                    groupContracted: '<i class="fa fa-plus-square-o"/>'
                },
                onRowClicked: function (params) {
                    var node = params.node;

                    currentItem = node.data;
                    
                    var path = node.data.name;
                    while (node.parent) {
                        node = node.parent;
                        path = node.data.name + app.status.directorySeparator + path;
                    }
                    path = path.replace("//", "/");

                    if (!currentItem.open && currentItem.folder) {
                        loadPathInfo(path);
                    }
                    currentItem.open = !currentItem.open;

                    console.log(path);
                }
            };

            new agGrid.Grid(element[0], gridOptions);

            function loadPathInfo(path) {
                $http
                        .post('/api/v1/utils/filesystem', {path: path})
                        .success(function (data) {
                            if (rowData.length === 0) {
                                rowData = data;
                            } else {
                                currentItem.children = data;
                            }
                            gridOptions.api.setRowData(rowData);
                        });
            }
            ;

            loadPathInfo("");
        }
    };
});