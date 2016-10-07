app.controller('TablesController', function ($scope, $http, $location) {

    if (!app.status.inited) {
        $location.path('/setup');
        return;
    }

    $scope.data = [];

    $http
            .get('/api/v1/tables')
            .success(function (data) {
                $scope.data = data;
            })
            .error(function (error) {
                errorHandler(error);
            });
});