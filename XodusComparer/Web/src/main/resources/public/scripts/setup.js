app.controller('SetupController', function () {
});

app.controller('SetupNewController', function ($scope, $http, $location, status) {
    $scope.predefinedKeys = [
        {name: 'Hub', key: 'jetPassServerDb'},
        {name: 'YouTrack', key: 'teamsysstore'}
    ];

    $scope.applyKey = function (item) {
        $scope.data.key = item.key;
    };

    $scope.data = {};

    $scope.submit = function () {
        if ($scope.setupForm.$valid) {
            $scope.loading = true;
            $http
                    .post('/api/v1/utils/compare', $scope.data)
                    .success(function (data) {
                        $scope.loading = false;
                        status.init();
                        $location.path('/tables');
                    });
        }
    };
});

app.controller('SetupLoadController', function ($scope, $http, $location, status) {
    $scope.data = {};

    $scope.submit = function () {
        if ($scope.setupForm.$valid) {
            $scope.loading = true;
            $http
                    .post('/api/v1/utils/load', $scope.data)
                    .success(function (data) {
                        $scope.loading = false;
                        status.init();
                        $location.path('/tables');
                    });
        }
    };
});