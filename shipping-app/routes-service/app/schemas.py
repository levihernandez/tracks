from pydantic import BaseModel
from datetime import datetime
from typing import Optional, Dict

class RouteBase(BaseModel):
    origin_lat: float
    origin_lng: float
    destination_lat: float
    destination_lng: float
    estimated_duration: Optional[int] = None

class RouteCreate(RouteBase):
    pass

class Route(RouteBase):
    id: int
    current_lat: Optional[float] = None
    current_lng: Optional[float] = None
    eta: Optional[datetime] = None
    status: Optional[str] = None
    traffic_conditions: Optional[Dict] = None
    created_at: datetime
    updated_at: datetime

    class Config:
        from_attributes = True  # Changed from orm_mode = True

class CheckpointBase(BaseModel):
    latitude: float
    longitude: float
    status: str

class CheckpointCreate(CheckpointBase):
    pass

class Checkpoint(CheckpointBase):
    id: int
    route_id: int
    timestamp: datetime
    created_at: datetime

    class Config:
        from_attributes = True  # Changed from orm_mode = True

class LocationUpdate(BaseModel):
    latitude: float
    longitude: float