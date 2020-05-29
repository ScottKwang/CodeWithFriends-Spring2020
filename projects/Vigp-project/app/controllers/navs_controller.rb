class NavsController < ApplicationController
  def home
    if user_signed_in?
      @participate = current_user.participations.order(:date)
      @hosting = current_user.events.where("date >= ?", Date.today)
    end
  end
end
