app.controller('DataViewController', function ($scope, $routeParams, $http) {
    var dataView = this;
    dataView.searchQuery = searchQuery();
    dataView.pageSize = 50;
    $scope.type = $scope.selectedType();
    $scope.$on('$routeUpdate', function () {
        dataView.searchQuery = searchQuery();
        dataView.pager = newPager(dataView.searchQuery);
        dataView.pager.pageChanged(1);
    });
    dataView.pager = newPager(dataView.searchQuery);

    //comment this if you want to load data on view show
    dataView.pager.pageChanged(1);

    dataView.refresh = function () {
        dataView.pager.pageChanged();
    };

    dataView.blobLink = function (entity, blob) {
        return 'api/type/' + entity.typeId + '/entity/' + entity.id + "/blob/" + blob.name;
    };

    function newPager(searchTerm) {
        return {
            totalCount: 0,
            items: [],
            currentPage: 1,
            expanded: {},
            pageChanged: function () {
                var pageNo = this.currentPage;
                var offset = (pageNo - 1) * dataView.pageSize;
                var self = this;
                self.currentPage = pageNo;

                $http.get('api/type/' + $scope.selectedType().id + '/entities', {
                    params: {
                        q: searchTerm,
                        offset: offset,
                        pageSize: (pageSize ? pageSize : 50)
                    }
                }).then(function (data) {
                    self.items = data.items;
                    self.totalCount = data.totalCount;
                    dataView.isSearchExecuted = true;
                });
            },
            hasPagination: function () {
                return this.totalCount > dataView.pageSize;
            },
            hasResults: function () {
                return this.items.length > 0;
            },
            expand: function (entity) {
                this.expanded[entity.id] = true;
            },
            isExpanded: function (entity) {
                return angular.isDefined(this.expanded[entity.id]);
            }
        };
    }

    function searchQuery() {
        return $routeParams.q ? $routeParams.q : null;
    }
});