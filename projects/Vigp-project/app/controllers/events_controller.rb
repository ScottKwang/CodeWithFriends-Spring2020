class EventsController < ApplicationController
  before_action :authenticate_user!
  before_action :find_resource, only: %i[show edit update join quit]

  def index
    @events = Event.all
  end

  def new
    @event = Event.new
  end

  def show
    @can_participate = current_user != @event.user && !@event.participations.include?(current_user) && ((@event.participations < @event.max_user) || (event.max_user = 0))
    @eventStartingSoon = Time.now.year - @event.date.year == 0 &&
      Time.now.day - @event.date.day == 0 &&
      Time.now.hour - @event.date.hour == 0

  end

  def edit; end

  def update
    if @event.update(event_params)
      redirect_to event_path(@event), notice: 'Event updated'
    else
      redirect_to edit_event_path(@event), alert:  @event.errors.full_messages.first
    end
  end

  def create
    @event = Event.new(event_params)
    @event.user = current_user
    if @event.save
      redirect_to event_path(@event), notice: 'Event create and ready to receive participants'
    else
      redirect_to new_event_path(), alert:  @event.errors.full_messages.first
    end
  end

  def join
    user = User.find(params[:user])
    if Participant.create(event: @event, user: user)
      redirect_to event_path(@event), notice: 'Successfully join the event'
    else
      render :show
    end
  end

  def quit
    user = User.find(params[:user])
    if Participant.where(event: @event, user: user).destroy_all
      redirect_to event_path(@event), notice: 'Successfully quit the event'
    else
      render :show
    end
  end

  private

  def event_params
    params.require(:event).permit(:title, :description, :date, :tag_list, :movie, :discord_url, :max_user)
  end

  def find_resource
    @event = Event.find(params[:id])
  end
end
