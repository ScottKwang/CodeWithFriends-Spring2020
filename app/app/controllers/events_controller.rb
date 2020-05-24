class EventsController < ApplicationController
  before_action :authenticate_user!
  before_action :find_resource, only: [:show]

  def index
    @events = Event.all
  end

  def new
    @event = Event.new()
  end

  def create

    @event = Event.new(event_params)
    @event.user = current_user
    if @event.save
      redirect_to events_path, notice: 'Event create and ready to receive participants'
    else
      render :new
    end
  end

  private

  def event_params
    params.require(:event).permit(:title, :description, :date, :tag_list, :movie)
  end

  def find_resource
    @event = Event.find(params[:id])
  end
end
