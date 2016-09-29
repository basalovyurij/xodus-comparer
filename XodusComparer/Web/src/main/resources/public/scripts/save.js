app.controller('SaveController', function ($scope, $http, $location) {
    
    $scope.submit = function () {
        if ($scope.setupForm.$valid) {
            $scope.loading = true;
            $http
                    .post('/api/v1/utils/load', $scope.data)
                    .success(function (data) {
                        $scope.loading = false;
                        $location.path('/tables');
                    });
        }
    };
});