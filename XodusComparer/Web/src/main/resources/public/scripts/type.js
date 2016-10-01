app.controller('TypeController', function ($scope, $http, $routeParams, $location) {
    
    if (!app.status.inited) {
        $location.path('/setup');
        return;
    }

    var name = $routeParams.name;

    $scope.table = [];

    $http
            .get('/api/v1/tables/' + name)
            .success(function (data) {
                $scope.table = data;
            });
});