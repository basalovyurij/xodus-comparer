app.directive('filebrowser', function ($uibModal) {
    return {
        restrict: 'E',
        scope: {
            ngId: '@',
            ngModel: '=',
            placeholder: '@'
        },
        templateUrl: '../../views/file-browser.html',
        link: function (scope) {
            scope.showModal = function () {
                $uibModal.open({
                    ariaLabelledBy: 'modal-title',
                    ariaDescribedBy: 'modal-body',
                    size: 'lg',
                    templateUrl: '../../views/file-browser-modal.html',
                    controller: 'FileBrowserModalInstanceCtrl',
                    controllerAs: '$ctrl',
                    scope: scope,
                    resolve: {
                        status: function () {
                            return app.status;
                        }
                    }
                });
            };
        }
    };
});

app.controller('FileBrowserModalInstanceCtrl', function ($scope, $http, $uibModalInstance) {
    var $ctrl = this;
    $ctrl.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };



    var columnDefs = [
        {
            headerName: "Name",
            field: "name",
            width: 600,
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

            $scope.$parent.ngModel = path;
        }
    };



    setTimeout(function () {
        var $element = angular.element(document.getElementById('file-browser'));
        $element.addClass('ag-file-browser');

        new agGrid.Grid($element[0], gridOptions);

        loadPathInfo("");
    });




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
});