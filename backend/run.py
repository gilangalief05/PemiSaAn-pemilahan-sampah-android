import ngrok

from flaskr import create_app
from flask_cors import CORS

app = create_app()
CORS(app, resources={r"/api/*": {"origins": "http://localhost:5000"}})

if __name__ == '__main__':
    #app.run(host="127.0.0.1", port=5000)
    app.run(debug=True)
