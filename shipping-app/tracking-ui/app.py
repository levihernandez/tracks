from flask import Flask, render_template, request, jsonify
import requests
import os
from datetime import datetime
from geopy.geocoders import Nominatim
from geopy.exc import GeocoderTimedOut

app = Flask(__name__)

SHIPPING_SERVICE_URL = os.getenv('SHIPPING_SERVICE_URL', 'http://shipping-service:8080')
ROUTES_SERVICE_URL = os.getenv('ROUTES_SERVICE_URL', 'http://routes-service:8000')

@app.route('/')
def index():
    return render_template('index.html')

def get_location_from_coordinates(lat, lng):
    try:
        geolocator = Nominatim(user_agent="shipping_tracker")
        location = geolocator.reverse(f"{lat}, {lng}")
        if location:
            address = location.raw.get('address', {})
            city = address.get('city', address.get('town', address.get('village', '')))
            state = address.get('state', '')
            return f"{city}, {state}" if city else "Location unavailable"
    except (GeocoderTimedOut, Exception):
        return "Location unavailable"
    return "Location unavailable"

@app.route('/tracking')
def track_order():
    order_number = request.args.get('order_id')
    if not order_number:
        return render_template('index.html', error="Please provide a tracking number")
    
    try:
        # Get order details by order number
        order_response = requests.get(f"{SHIPPING_SERVICE_URL}/api/orders/number/{order_number}")
        order_data = order_response.json()
        
        # Get current location name if coordinates exist
        current_location = "Unknown"
        if order_data.get('currentLat') and order_data.get('currentLng'):
            current_location = get_location_from_coordinates(
                order_data['currentLat'], 
                order_data['currentLng']
            )
        
        # Add location to tracking data
        tracking_data = {
            'order_number': order_data.get('orderNumber'),
            'status': order_data.get('status'),
            'origin': order_data.get('origin'),
            'destination': order_data.get('destination'),
            'cost': order_data.get('cost'),
            'current_lat': order_data.get('currentLat'),
            'current_lng': order_data.get('currentLng'),
            'current_location': current_location,
            'eta': order_data.get('estimatedTimeArrival'),
            'route_id': order_data.get('routeId')
        }
        
        # Get route details if available
        if tracking_data['route_id']:
            route_response = requests.get(f"{ROUTES_SERVICE_URL}/api/routes/{tracking_data['route_id']}")
            route_data = route_response.json()
            tracking_data['route'] = route_data
        
        return render_template('tracking.html', tracking=tracking_data)
    except requests.RequestException as e:
        return render_template('index.html', error="Unable to fetch tracking information")

@app.route('/api/tracking/<order_number>')
def get_tracking_update(order_number):
    try:
        # Get order by order number instead of ID
        order_response = requests.get(f"{SHIPPING_SERVICE_URL}/api/orders/number/{order_number}")
        order_data = order_response.json()
        
        # Get current location name
        current_location = "Unknown"
        if order_data.get('currentLat') and order_data.get('currentLng'):
            current_location = get_location_from_coordinates(
                order_data['currentLat'], 
                order_data['currentLng']
            )
        
        return jsonify({
            'status': order_data['status'],
            'current_lat': order_data['currentLat'],
            'current_lng': order_data['currentLng'],
            'current_location': current_location,
            'eta': order_data['estimatedTimeArrival']
        })
    except requests.RequestException as e:
        return jsonify({'error': 'Unable to fetch tracking updates'}), 500

@app.errorhandler(404)
def page_not_found(e):
    return render_template('404.html'), 404

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)