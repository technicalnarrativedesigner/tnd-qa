from dataclasses import dataclass


@dataclass(frozen=True)
class Settings:
    base_url: str
    standard_user: str
    standard_password: str
    locked_out_user: str
    locked_out_password: str
