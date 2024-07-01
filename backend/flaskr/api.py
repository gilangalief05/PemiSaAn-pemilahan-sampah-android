import os
import base64
import traceback
import json
from datetime import datetime

from flask import Blueprint, jsonify, request, render_template
from roboflow import Roboflow
from inference_sdk import InferenceHTTPClient

bp = Blueprint("api_bp", __name__)


rf = Roboflow(api_key="J9hk00UkWBz27Ra01GKw")
project = rf.workspace("t-x6ueg").project("waste-object-detection-hltsh")
version = project.version(1)
dataset = version.download("yolov7")

CLIENT = InferenceHTTPClient(
    api_url="https://detect.roboflow.com",
    api_key="J9hk00UkWBz27Ra01GKw"
)



@bp.route("/api/upload-photo", methods=["POST"])
def upload_photo():
    STORAGE_PATH = os.path.join(os.getcwd(), "resources", "public")
    timeNow = datetime.now().strftime("%Y%m%d-%H%M%S")
    FILE_PATH = STORAGE_PATH + "\\" + timeNow + ".png"

    print (FILE_PATH)
    if request.method == "POST":
        try:
            image = request.json["image"]
            with open(os.path.join(STORAGE_PATH, f"{timeNow}.png"), "wb") as f:
                f.write(base64.b64decode(image))
                result = CLIENT.infer(FILE_PATH, model_id="waste-object-detection-hltsh/1")['predictions']
                if not result:
                    return jsonify({'class': 'No object detected'})
                print(result)

            return result[0], 200
        except:
            traceback.print_exc()
        
        return jsonify({
            'status': 'error',
            'message': 'Internal server error'
        }), 500
#
