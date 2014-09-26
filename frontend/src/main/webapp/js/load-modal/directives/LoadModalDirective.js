angular.module('loadModalApp').directive('loadModalControl', function ($modal) {
    return {
        restrict: 'EA',
        scope: {
            displayModal: '='
        },
        controller: function($scope, $element, $modal) {
            $scope.modalInstance = {};
            $scope.$watch('displayModal', function(newVal, oldVal) {
                if(newVal) {
                    $scope.modalInstance = $modal.open({
                        templateUrl: '../../../templates/load-modal.html',
                        backdrop: 'static'
                    });
                } else {
                    $scope.modalInstance.dismiss("close");
                }
            });
        }
    }
});