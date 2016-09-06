angular.module('app.services', []).factory('Shipwreck', function($resource) {
  return $resource('/api/v1/shipwrecks/:id', { id: '@id' }, {
    update: {
      method: 'PUT'
    }
  });
}).factory('ShipMetaData', function($resource) {
    return $resource('/api/ships/:id', { id: '@id' }, {
        update: {
            method: 'PUT'
        }
    });
}).factory('Seaport', function($resource) {
    return $resource('/api/seaports/:id', { id: '@id' }, {
        update: {
            method: 'PUT'
        }
    });
}).service('popupService',function($window){
    this.showPopup=function(message){
        return $window.confirm(message);
    }
});
