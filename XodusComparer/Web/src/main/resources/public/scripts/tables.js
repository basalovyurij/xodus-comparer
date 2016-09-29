app.controller('TablesController', function ($scope, $http) {
    $scope.data = [];

    $http
            .get('/api/v1/tables')
            .success(function (data) {
                $scope.data = data;
            });
});