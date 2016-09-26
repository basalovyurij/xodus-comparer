app.directive('entityview', function () {
    return {
        restrict: 'E',
        scope: {
            item: '&'
        },
        templateUrl: '../../views/entity-view.html'
    };
});