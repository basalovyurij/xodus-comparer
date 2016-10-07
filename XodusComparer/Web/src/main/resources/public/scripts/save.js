app.controller('SaveController', function ($scope, $http, $location) {

    if (!app.status.inited) {
        $location.path('/setup');
        return;
    }

    $scope.submit = function () {
        if ($scope.setupForm.$valid) {
            $scope.loading = true;
            $http
                    .post('/api/v1/utils/load', $scope.data)
                    .success(function (data) {
                        $scope.loading = false;
                        $location.path('/tables');
                    })
                    .error(function (error) {
                        $scope.loading = false;
                        errorHandler(error);
                    });
        }
    };
});