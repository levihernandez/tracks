from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
from . import models, schemas, database
from datetime import datetime

app = FastAPI()

@app.post("/api/routes/", response_model=schemas.Route)
def create_route(route: schemas.RouteCreate, db: Session = Depends(database.get_db)):
    db_route = models.Route(**route.dict())
    db.add(db_route)
    db.commit()
    db.refresh(db_route)
    return db_route

@app.get("/api/routes/{route_id}", response_model=schemas.Route)
def get_route(route_id: int, db: Session = Depends(database.get_db)):
    route = db.query(models.Route).filter(models.Route.id == route_id).first()
    if route is None:
        raise HTTPException(status_code=404, detail="Route not found")
    return route

@app.post("/api/routes/{route_id}/checkpoints/", response_model=schemas.Checkpoint)
def create_checkpoint(
    route_id: int,
    checkpoint: schemas.CheckpointCreate,
    db: Session = Depends(database.get_db)
):
    db_checkpoint = models.Checkpoint(**checkpoint.dict(), route_id=route_id, timestamp=datetime.utcnow())
    db.add(db_checkpoint)
    db.commit()
    db.refresh(db_checkpoint)
    return db_checkpoint

@app.get("/api/routes/{route_id}/eta")
def calculate_eta(route_id: int, db: Session = Depends(database.get_db)):
    route = db.query(models.Route).filter(models.Route.id == route_id).first()
    if route is None:
        raise HTTPException(status_code=404, detail="Route not found")
    return {"eta": route.eta}

@app.put("/api/routes/{route_id}/location")
def update_current_location(
    route_id: int,
    location: schemas.LocationUpdate,
    db: Session = Depends(database.get_db)
):
    route = db.query(models.Route).filter(models.Route.id == route_id).first()
    if route is None:
        raise HTTPException(status_code=404, detail="Route not found")
    
    route.current_lat = location.latitude
    route.current_lng = location.longitude
    route.updated_at = datetime.utcnow()
    
    db.commit()
    db.refresh(route)
    return route