app.directive('entityview', function () {
    return {
        restrict: 'E',
        scope: {
            item: '&'
        },
        templateUrl: '../../views/entity-view.html',
        link: function(scope) {
            scope.dataView = scope.$parent.dataView;
        }
    };
});