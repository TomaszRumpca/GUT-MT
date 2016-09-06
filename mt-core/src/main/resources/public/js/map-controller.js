var MapController = function ($scope, $http, uiGmapGoogleMapApi) {

    var promiseMetaData = $http.get("api/forecast/meta");

    promiseMetaData.then(function (response) {
        var forecastMetaData = response.data;
        $scope.forecastMetaData = forecastMetaData;
        var centerLat = forecastMetaData.leftBottomLatCoordinate +
            (forecastMetaData.latStep * forecastMetaData.latDataCount / 2);
        var centerLon = forecastMetaData.leftBottomLonCoordinate +
            (forecastMetaData.lonStep * forecastMetaData.lonDataCount / 2);
        $scope.map = {center: {latitude : centerLat, longitude: centerLon}, zoom: 6};
        $scope.polyline =
            {
                id: 1,
                path: [
                    {
                        latitude: forecastMetaData.leftBottomLatCoordinate,
                        longitude: forecastMetaData.leftBottomLonCoordinate
                    },
                    {
                        latitude: forecastMetaData.leftBottomLatCoordinate,
                        longitude: forecastMetaData.leftBottomLonCoordinate + (forecastMetaData.lonStep * forecastMetaData.lonDataCount)
                    },
                    {
                        latitude: forecastMetaData.leftBottomLatCoordinate + (forecastMetaData.latStep * forecastMetaData.latDataCount),
                        longitude: forecastMetaData.leftBottomLonCoordinate + (forecastMetaData.lonStep * forecastMetaData.lonDataCount)
                    },
                    {
                        latitude: forecastMetaData.leftBottomLatCoordinate + (forecastMetaData.latStep * forecastMetaData.latDataCount),
                        longitude: forecastMetaData.leftBottomLonCoordinate
                    },
                    {
                        latitude: forecastMetaData.leftBottomLatCoordinate,
                        longitude: forecastMetaData.leftBottomLonCoordinate
                    }
                ],
                stroke: {
                    color: '#6060FB',
                    weight: 3
                },
                editable: true,
                draggable: true,
                geodesic: true,
                visible: true
            };

        $scope.arrow =
        {
            id: 2,
            path: [
                {
                    latitude: 48.802824,
                    longitude: 13.236774
                },
                {
                    latitude: 48.802824 + 0.032,
                    longitude: 13.236774 + 0.032
                }
            ],
            stroke: {
                color: '#000000',
                weight: 1
            },
            editable: true,
            draggable: true,
            geodesic: true,
            visible: true,
            icons: [{
                icon: {
                    path: google.maps.SymbolPath.FORWARD_OPEN_ARROW
                },
                offset: '20px',
                repeat: '20px'
            }]
        };

    });

    //$scope.map = {center: {latitude : centerLat, longitude: centerLon}, zoom: 6};
    $scope.map = {center: {latitude : 0, longitude: 0}, zoom: 6};
    uiGmapGoogleMapApi.then(function(maps) {
        console.log('Google Maps loaded');
    });


    $scope.places = [
        {
            id: 583187,
            latitude: 48.802824,
            longitude: 13.236774,
            title: "origin"
        },
        {
            id: 583188,
            latitude: 48.952824,
            longitude: 13.236774,
            title: "goal"
        }
    ];

    var userInput = {
        originCoords: {
            latitude: 48.802824,
            longitude: 13.236774
        },
        goalCoords: {
            latitude: 48.952824,
            longitude: 13.386774
        }
    };

    var promiseSolution = $http.post("api/solve",
        {
            params: {
                userInput: userInput
            }
        });

    promiseSolution.then(function (response) {
        $scope.solution = response.data;
    });


};
angular.module('app.controllers', ['uiGmapgoogle-maps'])
    .config(function(uiGmapGoogleMapApiProvider) {
        uiGmapGoogleMapApiProvider.configure({
            libraries: 'geometry,visualization'
        });
    })
    .controller("MapController", MapController);
