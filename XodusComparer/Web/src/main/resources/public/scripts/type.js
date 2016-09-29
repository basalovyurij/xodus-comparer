app.controller('TypeController', function ($scope, $http, $routeParams) {
    
    var name = $routeParams.name;
    
    $scope.table = [];

    $http
            .get('/api/v1/tables/' + name)
            .success(function (data) {
                $scope.table = data;
            });
});