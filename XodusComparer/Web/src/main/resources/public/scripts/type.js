app.controller('TypeCtrl', function ($scope, $http, $routeParams) {
    
    var name = $routeParams.name;
    
    $scope.data = [];

    $http
            .get('/api/v1/tables/' + name)
            .success(function (data) {
                $scope.data = data;
            });
});