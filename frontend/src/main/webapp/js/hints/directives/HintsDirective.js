angular.module('hintsApp').directive("hints", function ($sce) {
        return {
            restrict: 'AE',
            scope: {
                exercise: '='
            },
            templateUrl: '../templates/hints.html',
            controller: function ($scope, $sce) {
                $scope.getHtmlSafeString = function (unsafeString) {
                    return $sce.trustAsHtml(unsafeString);
                }
            }
        }
    }
);