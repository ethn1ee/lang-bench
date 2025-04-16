import time
import os
import psutil
import gc


class _Process:
    def __init__(self):
        self.start_memory = -1
        self.end_memory = -1
        self.start_time = -1
        self.end_time = -1

    def get_memory_used(self):
        """
        Returns:
            The memory used by the process with the given ID
        Raises:
            ValueError: If the process has not ended
        """
        if self.end_memory == -1:
            raise ValueError("The process has not ended!")

        return (self.end_memory - self.start_memory) / 1000

    def get_time_elapsed(self):
        """
        Returns:
            The time taken by the process with the given ID
        Raises:
            ValueError: If the process has not ended 
        """
        if self.end_time == -1:
            raise ValueError("The process has not ended!")

        return self.end_time - self.start_time


class Profiler:
    _processes = {}

    @staticmethod
    def _get_memory():
        mem = psutil.virtual_memory()
        return mem.total - mem.available

    @classmethod
    def create_process(cls, id: str) -> _Process:
        cls._processes[id] = _Process()
        return cls._processes[id]

    @classmethod
    def start(cls, id: str):
        current = cls.create_process(id)
        gc.collect()
        current.start_memory = cls._get_memory()
        current.start_time = time.time()

    @classmethod
    def end(cls, id: str):
        if id not in cls._processes:
            raise ValueError(f"No process found with id: {id}")
        current = cls._processes[id]
        gc.collect()
        current.end_memory = cls._get_memory()
        current.end_time = time.time()

    @classmethod
    def get_table(cls):
        sb = []
        sb.append(f"{'Stage':<20} | {'Memory (KB)':<20} | {'Time (s)':<20}")
        sb.append("-" * 21 + "|" + "-" * 22 + "|" + "-" * 21)

        total_row = None
        for key, proc in cls._processes.items():
            try:
                mem = proc.get_memory_used()
                t = proc.get_time_elapsed()
                row = f"{key:<20} | {mem:<20.3f} | {t:<20.3f}"

                if key == "Total":
                    total_row = row
                else:
                    sb.append(row)
            except Exception as e:
                sb.append(f"{key:<20} | ERROR: {e}")
        
        if total_row:
            sb.append(total_row)
    
        return "\n".join(sb)
