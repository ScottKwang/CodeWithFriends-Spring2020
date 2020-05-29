class UsersController < ApplicationController
  before_action :authenticate_user!
  before_action :find_resource, only: [:show]

  def show

  end

  private

  def find_resource
    @user = User.find(params[:id])
    @upcomingHost = @user.events.where("date > ?", Date.today)
    @pastHost = @user.events.where("date <= ?", Date.today)
    @upcomingPart = @user.participations.where("date > ?", Date.today)
    @pastPart = @user.participations.where("date <= ?", Date.today)
  end
end
